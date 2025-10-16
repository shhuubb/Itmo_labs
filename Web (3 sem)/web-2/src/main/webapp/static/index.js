(function(){

  const Y_VALUES = [-2, 1.5, -1, -0.5, 0, 0.5, 1, 1.5, 2];

  const yBox = document.getElementById('y-controls');
  const form = document.getElementById('point-form');
  const resultsBody = document.querySelector('#results-table tbody');
  const openBtn = document.getElementById('open-results');
  const modal = document.getElementById('results-modal');
  const xInput = document.getElementById('x');
  const rInputText = document.getElementById('r');
  const errorsBox = document.getElementById('errors');
  const plot = document.getElementById('plot');
  const ctx = plot.getContext('2d');

  let latestPoint = null;
  const contextPath = (typeof window.__CTX__ === 'string') ? window.__CTX__ : '';

  function setCanvasSize() {
    const dpr = window.devicePixelRatio || 1;
    plot.width = plot.offsetWidth * dpr;
    plot.height = plot.offsetHeight * dpr;
    ctx.scale(dpr, dpr);
  }

  function createControl(value, name, type){
    const wrap = document.createElement('label');
    wrap.className = 'control';
    const input = document.createElement('input');
    input.type = type;
    input.name = name;
    input.value = String(value);
    input.style.margin = '0 4px 0 0';
    if (type === 'checkbox'){
      let wasChecked = false;
      wrap.addEventListener('pointerdown', ()=>{ wasChecked = input.checked; });
      wrap.addEventListener('click', (e)=>{
        if (wasChecked){
          input.checked = false;
          input.dispatchEvent(new Event('change', { bubbles:true }));
          e.preventDefault();
          e.stopPropagation();
        }
      });
      input.addEventListener('keydown', (e)=>{
        if ((e.key === ' ' || e.key === 'Enter') && input.checked){
          input.checked = false;
          input.dispatchEvent(new Event('change', { bubbles:true }));
          e.preventDefault();
        }
      });
    }
    wrap.append(input, document.createTextNode(' ' + value));
    return wrap;
  }

  Y_VALUES.forEach(v => yBox.appendChild(createControl(v, 'y', 'radio')));

  function setErrors(messages){
    if (!errorsBox) return;
    errorsBox.innerHTML = '';
    if (!messages || messages.length === 0){
      errorsBox.style.display = 'none';
      return;
    }
    errorsBox.style.display = '';
    messages.forEach(msg => {
      const div = document.createElement('div');
      div.className = 'err';
      div.textContent = msg;
      errorsBox.appendChild(div);
    });
  }

  function validateCollect(){
    const xRaw = xInput.value;
    const xVal = xRaw.replace(',', '.');
    const xProvided = xVal.trim() !== '';
    const x = xProvided ? Number(xVal) : NaN;

    const yChecked = [...document.querySelectorAll('input[name="y"]:checked')].map(i => Number(i.value));
    const yProvided = yChecked.length > 0;
    const y = yProvided ? yChecked[0] : NaN;

    const rRaw = rInputText.value;
    const rVal = rRaw.replace(',', '.');
    const rProvided = rVal.trim() !== '';
    const r = rProvided ? Number(rVal) : NaN;

    const messages = [];
    const missing = [];
    if (!xProvided) missing.push('X');
    if (!yProvided) missing.push('Y');
    if (!rProvided) missing.push('R');
    if (missing.length > 0){
      messages.push('Необходимо выбрать/ввести: ' + missing.join(', '));
    }
    if (xProvided && !Number.isFinite(x)) messages.push('X должен быть числом');
    if (Number.isFinite(x) && (x <= -3 || x >= 5)) messages.push('X должен быть в диапазоне (-3, 5)');

    if (yProvided && !Y_VALUES.includes(y)) messages.push('Y должен быть выбран из списка');
    if (yChecked.length > 1) messages.push('Можно выбрать только одно значение Y');

    if (rProvided && !Number.isFinite(r)) messages.push('R должен быть числом');
    if (Number.isFinite(r) && (r <= 2 || r >= 5)) messages.push('R должен быть в диапазоне (2, 5)');

    return { x, y, r, messages };
  }

  function drawAxes(){
    const W = plot.offsetWidth, H = plot.offsetHeight;
    ctx.clearRect(0, 0, W, H);
    ctx.fillStyle = '#0b1324';
    ctx.fillRect(0, 0, W, H);
    ctx.strokeStyle = 'rgba(203,213,225,.9)';
    ctx.lineWidth = 1;

    ctx.beginPath();
    ctx.moveTo(40, H/2);
    ctx.lineTo(W-20, H/2);
    ctx.moveTo(W/2, 20);
    ctx.lineTo(W/2, H-20);
    ctx.stroke();


    ctx.fillStyle = '#9fb3c8';
    ctx.font = '12px system-ui, sans-serif';
    function label(x, y, t) { ctx.fillText(t, x, y); }
    const l = ['-R','-R/2','R/2','R'];
    const offs = [W/2-200, W/2-100, W/2+100, W/2+200];
    offs.forEach((x, i) => {
      ctx.beginPath();
      ctx.moveTo(x, H/2-5);
      ctx.lineTo(x, H/2+5);
      ctx.stroke();
      label(x-6, H/2+18, l[i]);
    });
    [H/2-200, H/2-100, H/2+100, H/2+200].forEach((y, i) => {
      ctx.beginPath();
      ctx.moveTo(W/2-5, y);
      ctx.lineTo(W/2+5, y);
      ctx.stroke();
      label(W/2+8, y+4, ['R','R/2','-R/2','-R'][i]);
    });


    ctx.fillStyle = 'rgba(37,99,235,.45)';
    ctx.fillRect(W/2, H/2-200, 200, 200);
    ctx.beginPath();
    ctx.moveTo(W/2+100, H/2);
    ctx.arc(W/2, H/2, 100, Math.PI/2, Math.PI, false);
    ctx.closePath();
    ctx.fill();

    if (latestPoint) {
      const { x, y, r} = latestPoint;
      const scale = 200 / r;
      const canvasX = W/2 + x * scale;
      const canvasY = H/2 - y * scale;
      if (canvasX >= 0 && canvasX <= W && canvasY >= 0 && canvasY <= H) {
        ctx.fillStyle = latestPoint.hit ? '#22c55e' : '#ef4444';
        ctx.beginPath();
        ctx.arc(canvasX, canvasY, 3, 0, 2 * Math.PI);
        ctx.fill();
      }
    }
  }

  function getRValue(){
    const rRaw = rInputText.value;
    const rVal = rRaw.replace(',', '.');
    const provided = rVal.trim() !== '';
    const r = provided ? Number(rVal) : NaN;
    if (!provided) return { ok:false, error:'Сначала укажите радиус R' };
    if (!Number.isFinite(r)) return { ok:false, error:'R должен быть числом' };
    if (r <= 2 || r >= 5) return { ok:false, error:'R должен быть в диапазоне (2, 5)' };
    return { ok:true, r };
  }

  function canvasToCoords(px, py, r){
    const W = plot.offsetWidth, H = plot.offsetHeight;
    const scale = 200 / r; // pixels per unit
    const x = (px - W/2) / scale;
    const y = (H/2 - py) / scale;
    return { x, y };
  }

  function toFixedNum(n, digits){
    const d = digits ?? 6;
    return Number(n.toFixed(d));
  }

  function snapYToAllowed(y){
    let best = Y_VALUES[0];
    let bestDiff = Math.abs(y - best);
    for (let i = 1; i < Y_VALUES.length; i++){
      const dv = Math.abs(y - Y_VALUES[i]);
      if (dv < bestDiff){
        best = Y_VALUES[i];
        bestDiff = dv;
      }
    }
    return best;
  }

  async function sendPoint(x, y, r){
    const url = `${contextPath}/controller?x=${encodeURIComponent(x)}&y=${encodeURIComponent(y)}&r=${encodeURIComponent(r)}`;
    const res = await fetch(url, { headers: { 'Accept': 'application/json' } });
    if (!res.ok) {
      const t = await res.text().catch(()=> '');
      let err = 'Ошибка запроса';
      try { const j = JSON.parse(t); if (j && j.error) err = j.error; } catch(e) {}
      throw new Error(err);
    }
    return res.json();
  }

  if (plot) {
    plot.addEventListener('click', async (e)=>{
      setErrors([]);
      const rRes = getRValue();
      if (!rRes.ok){
        setErrors([rRes.error]);
        return;
      }
      const rect = plot.getBoundingClientRect();
      const px = e.clientX - rect.left;
      const py = e.clientY - rect.top;
      const { x, y } = canvasToCoords(px, py, rRes.r);
      const xn = toFixedNum(x, 6);
      const yn = snapYToAllowed(y);
      try {
        const data = await sendPoint(xn, yn, rRes.r);
        appendRow(data);
        latestPoint = { x: data.x, y: data.y, r: data.r, hit: data.hit};
        drawAxes();
      } catch(err){
        setErrors([String(err.message || err)]);
      }
    });
  }

  function appendRow(item){
    const tr = document.createElement('tr');
    const creationTime = item.creationTime ?? item.now ?? 'Неизвестно';
    const hit = (typeof item.IsHit !== 'undefined') ? item.IsHit : item.hit;
    tr.innerHTML = `<td>${creationTime}</td><td>${item.x}</td><td>${item.y}</td><td>${item.r}</td><td><span class="badge ${hit?'':'fail'}">${hit? 'Да' : 'Нет'}`;
    if (resultsBody) resultsBody.prepend(tr);
  }

  form.addEventListener('submit', (e)=>{
    setErrors([]);
    const { messages } = validateCollect();
    if (messages.length > 0){
      e.preventDefault();
      setErrors(messages);
    }
  });

  setCanvasSize();
  drawAxes();

  (function resetControls(){
    form.reset();
    [...document.querySelectorAll('input[name="y"]')].forEach(i => { i.checked = false; });
    xInput.value = '';
    rInputText.value = '';
    setErrors([]);
  })();

  window.addEventListener('resize', () => {
    setCanvasSize();
    drawAxes();
  });

  (function hydrate(){
    const el = document.getElementById('initial-results');
    const items = el ? JSON.parse(el.textContent || '[]') : [];
    items.forEach(appendRow);
    if (items.length > 0) {
      const last = items[items.length - 1];
      latestPoint = { x: last.x, y: last.y, r: last.r, hit: last.hit};
      drawAxes();
    }
  })();

  function openModal(){
    if (!modal) return;
    modal.classList.remove('hidden');
    modal.setAttribute('aria-hidden', 'false');
    document.body.style.overflow = 'hidden';
  }

  function closeModal(){
    if (!modal) return;
    modal.classList.add('hidden');
    modal.setAttribute('aria-hidden', 'true');
    document.body.style.overflow = '';
  }

  if (openBtn) {
    openBtn.addEventListener('click', openModal);
  }
  if (modal) {
    modal.addEventListener('click', (e)=>{
      if (e.target && e.target.hasAttribute('data-close')) closeModal();
    });
    window.addEventListener('keydown', (e)=>{
      if (e.key === 'Escape') closeModal();
    });
  }

})();
