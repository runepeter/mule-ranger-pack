/* Start Gauge Code */

/* Start Controller Code */
window.addEvent('domready', function() {
	
	function getUpdate(){	
		new Request.JSON({
                method: 'GET',
				url: 'rest/file/statistics/avg-execution-time.json',
				onComplete: function(jsonObj) {

					var mydata = jsonObj.mygauge;

                    var avg = Number(mydata[0].endval);

                    //$('mydiv').set('html','<div style="background-color: #e0e0e0; border-bottom: 1px solid #000000; padding-left:2px;">RUNE: ' + avg + '</div>');

                    window.document.meter.SetVariable("mValue", avg);
				}
			}).send();
		}
	var intUpdate = getUpdate.periodical(1000);
});
 