(async function() {
    // if TICKER is null then return
    if(!TICKER) return;

    console.log(TICKER);

    let candles = await (await fetch("/api/stocks/candles/" + TICKER)).json();
    let barData = [];
    for(candle of candles) {
        let [c, h, l, o, x, _] = candle;
        barData.push({c, h, l, o, x});
    }

    console.log(barData);

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

    var getRandomInt = function(max) {
        return Math.floor(Math.random() * Math.floor(max));
    };

    function randomNumber(min, max) {
        return Math.random() * (max - min) + min;
    }

    function randomBar(date, lastClose) {
        var open = +randomNumber(lastClose * 0.95, lastClose * 1.05).toFixed(2);
        var close = +randomNumber(open * 0.95, open * 1.05).toFixed(2);
        var high = +randomNumber(Math.max(open, close), Math.max(open, close) * 1.1).toFixed(2);
        var low = +randomNumber(Math.min(open, close) * 0.9, Math.min(open, close)).toFixed(2);
        return {
            x: date.valueOf(),
            o: open,
            h: high,
            l: low,
            c: close
        };

    }

})();