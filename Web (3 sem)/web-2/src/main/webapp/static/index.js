(function(){

  const Y_VALUES = [-2, 1.5, -1, -0.5, 0, 0.5, 1, 1.5, 2];

  const yBox = document.getElementById('y-controls');
  const form = document.getElementById('point-form');
  const resultsBody = document.querySelector('#results tbody');
  const xInput = document.getElementById('x');
  const rInputText = document.getElementById('r');
  const errorsBox = document.getElementById('errors');
  const plot = document.getElementById('plot');
  const ctx = plot.getContext('2d');

  let latestPoint = null;

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

  Y_VALUES.forEach(v => yBox.appendChild(createControl(v, 'y', 'checkbox')));

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
        ctx.fillStyle = 'red';
        ctx.beginPath();
        ctx.arc(canvasX, canvasY, 3, 0, 2 * Math.PI);
        ctx.fill();
      }
    }
  }

  function appendRow(item){
    const tr = document.createElement('tr');
    const creationTime = item.creationTime ?? item.now ?? 'Неизвестно';
    const hit = (typeof item.IsHit !== 'undefined') ? item.IsHit : item.hit;
    const exec = item.executionTime ?? item.execMs ?? 0;
    tr.innerHTML = `<td>${creationTime}</td><td>${item.x}</td><td>${item.y}</td><td>${item.r}</td><td><span class="badge ${hit?'':'fail'}">${hit? 'Да' : 'Нет'}</span></td><td>${exec}</td>`;
    resultsBody.prepend(tr);
  }

  form.addEventListener('submit', (e)=>{
    setErrors([]);
    const { messages } = validateCollect();
    if (messages.length > 0){
      e.preventDefault();
      setErrors(messages);
      return;
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

})();
