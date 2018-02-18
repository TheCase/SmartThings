/**
 *  App Endpoint API Access Example
 *
 *  Author: SmartThings
 */

definition(
    name: "DeviceState_API",
    namespace: "TheCase",
    author: "TheCase",
    description: "endpoint for reading device states",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience%402x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience%402x.png",
    oauth: true)

preferences {
	section("Allow Endpoint to Control These Things...") {
        input "temperature", "capability.temperatureMeasurement", title: "Which Temps?", multiple: true
        input "humidity", "capability.relativeHumidityMeasurement", title: "Which humidity sensor?", multiple: true
        input "power", "capability.powerMeter", title: "Which Power Meters?", multiple: true
        input "battery", "capability.battery", title: "Which Battery Powered Devices?", multiple: true
	input "status", "capability.status", title: "Which Status Reporters?", multiple: true
	}
}

mappings {
	path("/power") { 		action: [ GET: "listPower" ] }
	path("/temperature") { 		action: [ GET: "listTemperature" ] }
	path("/humidity") { 		action: [ GET: "listHumidity" ] }   
	path("/battery") { 		action: [ GET: "listBattery" ] }  
	path("/status") { 		action: [ GET: "listStatus" ] }   

}

def listPower() { 	power.collect{device(it,"powerMeter")} }
def listTemperature() { temperature.collect{device(it,"temperatureMeasurement")} }
def listHumidity() { 	humidity.collect{device(it,"relativeHumidityMeasurement")} }
def listBattery() { 	battery.collect{device(it,"battery")} }
def listStatus() { 	status.collect{device(it,"status")} }

private device(it, type) {
	def map = [ temperatureMeasurement: "temperature", 
    		    relativeHumidityMeasurement: "humidity",
                    powerMeter: "power",
                    battery: "battery",
		    status: "status"
                ] 
	def attrib = map[type]
    log.debug "${it.label} ${it.currentValue(attrib)}"
	it ? [id: it.id, label: it.label, type: type, value: it.currentValue(attrib) ] : null 
}
