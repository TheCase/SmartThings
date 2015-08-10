/**
 *  Green Thermostat
 *
 *  Author: lailoken@gmail.com
 *  Date: 2013-08-14
 */
preferences {
    section("Thermostat") {
		input "thermostat", "capability.thermostat"
	}
    
	section("Daytime (ThermMorning)") {
        input "homeHeat",  "decimal", title:"Heat (default 68)", required:false
        input "homeCool",  "decimal", title:"Cool (default 80)", required:false
    }
	section("At night (ThermNight)") {
        input "nightHeat", "decimal", title:"Heat (default 62)", required:false
        input "nightCool", "decimal", title:"Cool (default 80)", required:false
    }
	section("Away") {
        input "awayHeat",  "decimal", title:"Heat (default 50)", required:false
        input "awayCool",  "decimal", title:"Cool (default 90)", required:false
	}   
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	subscribe(thermostat, "heatingSetpoint", heatingSetpointHandler)
	subscribe(thermostat, "coolingSetpoint", coolingSetpointHandler)
	subscribe(thermostat, "temperature",     temperatureHandler)
	subscribe(location)
	subscribe(app)
}

def heatingSetpointHandler(evt)
{
	log.info "heatingSetpoint: $evt, $settings"
}

def coolingSetpointHandler(evt)
{
	log.info "coolingSetpoint: $evt, $settings"
}

def temperatureHandler(evt)
{
	log.info "currentTemperature: $evt, $settings"
}

def changedLocationMode(evt)
{
	log.info "changedLocationMode: $evt, $settings"

    def homeHeat  = homeHeat  ?: 68
    def homeCool  = homeCool  ?: 80
    def nightHeat = nightHeat ?: 62
    def nightCool = nightCool ?: 80
    def awayHeat  = awayHeat  ?: 50
    def awayCool  = awayCool  ?: 90
    
    if ( evt.value == "ThermMorning" ) {
		thermostat.setHeatingSetpoint(homeHeat)
		thermostat.setCoolingSetpoint(homeCool)
    }
    if ( evt.value == "ThermNight" ) {
		thermostat.setHeatingSetpoint(nightHeat)
		thermostat.setCoolingSetpoint(nightCool)
    }
    if ( evt.value == "Away" ) {
		thermostat.setHeatingSetpoint(awayHeat)
		thermostat.setCoolingSetpoint(awayCool)
    }

	thermostat.poll()
}

def appTouch(evt)
{
	log.info "appTouch: $evt, $settings"

	//thermostat.setHeatingSetpoint(heatingSetpoint)
	//thermostat.setCoolingSetpoint(coolingSetpoint)
	//thermostat.poll()
}

// catchall
def event(evt)
{
	log.info "value: $evt.value, event: $evt, settings: $settings, handlerName: ${evt.handlerName}"
}
