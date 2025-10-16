(function(){
  const plot = document.getElementById('result-plot');
  if (!plot) return;
  const ctx = plot.getContext('2d');
  const W = plot.width, H = plot.height;

  function drawAxesAndArea(){
    ctx.clearRect(0,0,W,H);
    ctx.fillStyle = '#0b1324';
    ctx.fillRect(0,0,W,H);
    ctx.strokeStyle = 'rgba(203,213,225,.9)';
    ctx.lineWidth = 1;
    ctx.beginPath();
    ctx.moveTo(40, H/2);
    ctx.lineTo(W-20, H/2);
    ctx.moveTo(W/2, 20);
    ctx.lineTo(W/2, H-20);
    ctx.stroke();

    ctx.fillStyle = 'rgba(37,99,235,.45)';
    ctx.fillRect(W/2, H/2-200, 200, 200);
    ctx.beginPath();
    ctx.moveTo(W/2+100, H/2);
    ctx.arc(W/2, H/2, 100, Math.PI/2, Math.PI, false);
    ctx.closePath();
    ctx.fill();
  }

  function drawPoint(x, y, r, hit){
    const scale = 200 / r;
    const cx = W/2 + x * scale;
    const cy = H/2 - y * scale;
    ctx.fillStyle = hit ? '#22c55e' : '#ef4444';
    ctx.beginPath();
    ctx.arc(cx, cy, 4, 0, 2*Math.PI);
    ctx.fill();
  }

  function run(){
    const x = Number(plot.getAttribute('data-x'));
    const y = Number(plot.getAttribute('data-y'));
    const r = Number(plot.getAttribute('data-r'));
    const hitAttr = plot.getAttribute('data-hit');
    const hit = String(hitAttr) === 'true';
    drawAxesAndArea();
    if (Number.isFinite(x) && Number.isFinite(y) && Number.isFinite(r)){
      drawPoint(x, y, r, hit);
    }
  }

  run();
})();


