(async function() {
    // if TICKER is null then return
    if(!TICKER) return;

    console.log(TICKER);

    let candles = await (await fetch("/api/stocks/candles/" + TICKER)).json();
    let barData = [];
    for(candle of candles) {
        let [c, h, l, o, x, _] = candle;
        x = new Date(x*1000);
        // offsite 1 day and 8 hours to match the requested date and time
        // -8 hours to match parcific time zone
        x.setHours(x.getHours() - 8 - 24);
        barData.push({c, h, l, o, x: x.valueOf()});
    }

    var ctx = document.getElementById('chart').getContext('2d');
    ctx.canvas.width = 1000;
    ctx.canvas.height = 250;

    function lineData() { return barData.map(d => { return { x: d.x, y: d.c} }) };

    var chart = new Chart(ctx, {
        type: 'candlestick',
        data: {
            datasets: [{
                label: TICKER + ' - stock candles',
                data: barData
            }]
        }
    });
})();