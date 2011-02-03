/* Start Gauge Code */

/* Start Controller Code */
window.addEvent('domready', function()
{
    function getUpdate()
    {
        new Request.JSON({
            method: 'GET',
            url: 'rest/file/statistics.json',
            onComplete: function(jsonObj)
            {
                var service = jsonObj.serviceStatistics.fileService;
                updateAverageExecutionTime(service);
                updateDigital(service);
            }
        }).send();
    }

    function updateAverageExecutionTime(service)
    {
        var range = window.document.meter.GetVariable("range");
        var value = Number(service.averageExecutionTime);

        if (value >= range)
        {
            range = value * 2;
            range = (range - new Number(range.toString().substr(1))) * 2;
            window.document.meter.SetVariable("range", range);
        }

        if (value <= range / 4 && value > 10)
        {
            range = range / 2;
            window.document.meter.SetVariable("range", range);
        }

        window.document.meter.SetVariable("mValue", value);
    }

    function updateDigital(service)
    {
        var value_a = Number(service.totalEventsReceived);
        var value_b = Number(service.executedEvents);
        window.document.digital.SetVariable("value_a", value_a);
        window.document.digital.SetVariable("value_b", value_b);
    }

    var intUpdate = getUpdate.periodical(1000);
});
 