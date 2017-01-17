# opium.agent
The agent used by Opium

# building the project 
`mvn clean package`

# starting the project
`java -jar opium-agent.jar -Dmaster=http://IP_OF_HIVE:2020`
By default the project will start on port 2021. If you don't send a `master` address, it will default to `http://localhost:2020/`

# device monitoring
Every 10 seconds the agent will monitor for connected Android devices. Once found they will be registered to the Hive.

# pre-requisits
- `adb` needst to be installed on the running host
- Appium needs to be installed on the running host

# build
[![Build Status](https://snap-ci.com/ludovicianul/opium.agent/branch/master/build_image)](https://snap-ci.com/ludovicianul/opium.agent/branch/master)
