(function(){
	'use strict';

	const X_VALUES = [-4,-3,-2,-1,0,1,2,3,4];
	const R_VALUES = [1,1.5,2,2.5,3];

	const xBox = document.getElementById('x-controls');
	const rBox = document.getElementById('r-controls');
	const form = document.getElementById('point-form');
	const resultsBody = document.querySelector('#results tbody');
	const yInput = document.getElementById('y');
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
		if (type === 'radio'){
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

	X_VALUES.forEach(v => xBox.appendChild(createControl(v, 'x', 'radio')));
	R_VALUES.forEach(v => rBox.appendChild(createControl(v, 'r', 'radio')));

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
		const xs = [...document.querySelectorAll('input[name="x"]:checked')].map(i => Number(i.value));
		const yRaw = yInput.value;
		const yVal = yRaw.replace(',', '.');
		const yProvided = yVal.trim() !== '';
		const y = yProvided ? Number(yVal) : NaN;
		const rInput = document.querySelector('input[name="r"]:checked');
		const rProvided = !!rInput;
		const r = rProvided ? Number(rInput.value) : NaN;

		const messages = [];
		const missing = [];
		if (xs.length === 0) missing.push('X');
		if (!yProvided) missing.push('Y');
		if (!rProvided) missing.push('R');
		if (missing.length > 0){
			messages.push('Необходимо выбрать/ввести: ' + missing.join(', '));
		}
		if (yProvided && !Number.isFinite(y)) messages.push('Y должен быть числом');
		if (Number.isFinite(y) && (y <= -3 || y >= 5)) messages.push('Y должен быть в диапазоне (-3, 5)');

		return { xs, y, r, messages };
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

		// Draw shapes
		ctx.fillStyle = 'rgba(37,99,235,.45)';
		ctx.fillRect(W/2-100, H/2-200, 100, 200);
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

	async function loadHistory(){
		const params = new URLSearchParams();
		params.append('history',"1")
		try {
			const res = await fetch(`calculate?${params}`, {
				method: 'POST',
				headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
			});
			const text = await res.text();
			let json;
			try {
				json = JSON.parse(text);
			} catch (_) {
				throw new Error(text || 'Failed to parse history');
			}
			if (!res.ok) {
				console.error('Server validation error:', json.error || text);
				throw new Error(json.error || 'Failed to load history');
			}

			resultsBody.innerHTML = '';
			if (json && Array.isArray(json.history) && json.history.length > 0) {
				json.history.forEach(appendRow);
				const latest = json.history[json.history.length-1];
				latestPoint = { x: latest.x, y: latest.y, r: latest.r };
			}
			setCanvasSize();
			drawAxes();
		} catch (err) {
			setErrors([err.message]);
		}
	}

	async function submit(xs, y, r){
		for(const x of xs){
			const params = new URLSearchParams();
			params.append('x', x);
			params.append('y', y.toString());
			params.append('r', r.toString());
			const url = `calculate?${params.toString()}`;
			const res = await fetch(url, { method: 'POST', headers: { 'Content-Type': 'application/x-www-form-urlencoded' } });
			const text = await res.text();
			let json;

			try {
				json = JSON.parse(text);
			} catch(_) {
				throw new Error(text || 'Bad response');
			}
			if (!res.ok) {
				console.error('Server validation error:', json.error || text);
				throw new Error(json.error || 'Request failed');
			}

			latestPoint = { x, y, r };
			setCanvasSize();
			drawAxes();

			resultsBody.innerHTML = '';
			if (json && Array.isArray(json.history)) {
				json.history.forEach(appendRow);
			}
		}
	}


	form.addEventListener('submit', async (e)=>{
		e.preventDefault();
		setErrors([]);
		const { xs, y, r, messages } = validateCollect();
		if (messages.length > 0){
			setErrors(messages);
			return;
		}
		await submit(xs, y, r);
	});

	setCanvasSize();
	drawAxes();
	loadHistory();

	(function resetControls(){
		form.reset();
		[...document.querySelectorAll('input[name="x"], input[name="r"]')].forEach(i => { i.checked = false; });
		yInput.value = '';
		setErrors([]);
	})();

	window.addEventListener('resize', () => {
		setCanvasSize();
		drawAxes();
	});
})();