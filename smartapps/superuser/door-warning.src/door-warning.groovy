/**
 *  Notify When Left Open during hours
 *
 *  Author: TheCase <smartthings@caseyclan.net>
 * 
 *  Date: 2013-11-15
 */
 
preferences {
	section("When . . .") {
		input "contactSensor", "capability.contactSensor", title: "Sensor is left open" 
       	input "numMinutes", "number", title: "For how many minutes"
		input "timeStart", "time", title: "Only from what time?", required: true
		input "timeEnd", "time", title: "Until what time?", required: true
        input "messageText", "text", title: "Send notification that says"
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}"
    initSettings()
}

def updated() {
    log.debug "Updated with settings: ${settings}"
	unsubscribe()
    initSettings()
}

def initSettings()
{
	log.debug "startTime: $timeStart, endTime: $timeEnd"
   	subscribe(contactSensor, "contact", onContactChange);
}

def onContactChange(evt) {
	def t0 = now()
	def timeZone = location.timeZone ?: timeZone(timeStart)
	def start = timeToday(tomeStart, timeZone)
	def end = timeToday(timeEnd, timeZone)
	log.debug "onContactChange -- startTime: ${start}, endTime: ${end}, t0: ${new Date(t0)}";    
	if (evt.value == "open") and (t0 >= start.time && t0 <= end.time){
    	runIn(numMinutes * 60, onContactLeftOpenHandler);
    } else {
    	unschedule(onContactLeftOpenHandler);
    }
}

def onContactLeftOpenHandler() {
	log.debug "onContactLeftOpenHandler message: ${messageText}";
	sendPush(messageText);
}
