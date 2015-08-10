/**
 * Off at time
 *
 * Author: The Case
 *
 * Date: 11/15/2013
 */
preferences {


section("What Time?") {
	input "offTime", "time", title: "When?", required: true
}


section("Which Device?"){
	input "onceOff", "capability.switch", multiple: true, required: true
}

section( "Notifications" ) {
	input "sendPushMessage", "enum", title: "Send a push notification?", metadata:[values:["Yes", "No"]], required: false

}

}

def installed() {
	log.debug "OffOnce Installed with time: $offTime devices: $onceOff"
	schedule(offTime, turnlightoff)
}

def updated(settings) {
	log.debug "OffOnce Updated with time: $offTime devices: $onceOff"
	unschedule()
	schedule(offTime, turnlightoff)
}

def turnlightoff() {
	log.debug "Turning off switches"
    onceOff.off()
}

private send(msg) {
	if ( sendPushMessage != "No" ) {
		log.debug( "OnceOff sending push message" )
		sendPush( msg )
	}
	log.debug msg
}