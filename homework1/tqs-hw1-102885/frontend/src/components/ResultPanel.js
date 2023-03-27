import React, { useState } from "react"
import {US_EPA_MAP, GB_DEFRA_MAP} from "../constants"
import DataCell from "./DataCell";
import { WiDaySunny, WiDaySunnyOvercast, WiNightClear, WiNightPartlyCloudy, WiCloudy, WiShowers, WiRain, WiThunderstorm, WiSnow, WiFog } from 'weather-icons-react';

function getWeatherIcon(condition, isDay) {
  switch(condition) {
    case 'Sunny':
    case 'Clear':
      return isDay ? <WiDaySunny className="condition" size={64} color='#fff' /> : <WiNightClear className="condition" size={64} color='#fff' />;
    case 'Partly cloudy':
      return isDay ? <WiDaySunnyOvercast className="condition" size={64} color='#fff' /> : <WiNightPartlyCloudy className="condition" size={64} color='#fff' />;
    case 'Cloudy':
    case 'Overcast':
      return <WiCloudy className="condition" size={64} color='#fff' />;
    case 'Mist':
    case 'Fog':
    case 'Freezing fog':
      return <WiFog className="condition" size={64} color='#fff' />;
    case 'Patchy rain possible':
    case 'Light rain':
    case 'Light freezing rain':
    case 'Patchy light rain':
    case 'Patchy light drizzle':
      return <WiRain className="condition" size={64} color='#fff' />;
    case 'Moderate rain at times':
    case 'Moderate rain':
    case 'Heavy rain at times':
    case 'Heavy rain':
      return <WiShowers className="condition" size={64} color='#fff' />;
    case 'Patchy snow possible':
    case 'Patchy sleet possible':
    case 'Patchy freezing drizzle possible':
    case 'Blowing snow':
    case 'Blizzard':
    case 'Patchy moderate snow':
    case 'Moderate or heavy snow':
    case 'Patchy heavy snow':
    case 'Heavy snow':
      return <WiSnow className="condition" size={64} color='#fff' />;
    case 'Thundery outbreaks possible':
    case 'Moderate or heavy showers of ice pellets':
    case 'Moderate or heavy rain with thunder':
      return <WiThunderstorm className="condition" size={64} color='#fff' />;
    case 'Patchy sleet showers':
    case 'Patchy light snow':
    case 'Light snow':
    case 'Moderate or heavy snow showers':
    case 'Patchy light rain with thunder':
    case 'Light sleet showers':
    case 'Light snow showers':
      return isDay ? <WiDaySunny className="condition" size={64} color='#fff' /> : <WiNightClear className="condition" size={64} color='#fff' />;
    case 'Torrential rain shower':
    case 'Light rain shower':
    case 'Moderate or heavy rain shower':
      return <WiShowers className="condition" size={64} color='#fff' />;
    default:
      return null;
  }
}

function ResultPanel({weatherData}) {
    const [aqIndex, setAqIndex] = useState('epa');

    return (
        <div className="panel">
            <h2 className="city" id="city">{weatherData.name}, {weatherData.country}</h2>
            <div className="weather" id="weather">
                <div className="header">
                    <div className="airquality" id="airquality">
                        <h1 className="aq" id="aq">
                            {getWeatherIcon(weatherData.condition, weatherData.isDay)}
                            <span id="num">{aqIndex === "epa" ? weatherData.usEpaIndex : weatherData.gbDefraIndex}</span>
                            <span className={`btn epa ${aqIndex === 'epa' ? 'active' : ''}`} id="epa" onClick={() => setAqIndex("epa")}>EPA</span>
                            <span className={`divider secondary`}>|</span>
                            <span className={`btn defra ${aqIndex === 'defra' ? 'active' : ''}`} id="defra" onClick={() => setAqIndex("defra")}>DEFRA</span>
                        </h1>
                    </div>
                    <div className="group secondary highlights">
                        <h3 id="dt">{weatherData.localtime}</h3>
                        <h3 id="description">{aqIndex === "epa" ? US_EPA_MAP[weatherData.usEpaIndex] : `${GB_DEFRA_MAP[weatherData.gbDefraIndex]} pollution banding`} </h3>
                    </div>
                </div>
                <div className="group secondary grid">
                    <div className="column">
                        <DataCell label="Carbon Monoxide" value={weatherData.co} measure="&#956;g/m&#179;" />
                        <DataCell label="Ozone" value={weatherData.o3} measure="&#956;g/m&#179;" />
                    </div>
                    <div className="column">
                        <DataCell label="Nitrogen dioxide" value={weatherData.no2} measure="&#956;g/m&#179;" />
                        <DataCell label="Sulphur dioxide" value={weatherData.so2} measure="&#956;g/m&#179;" />
                    </div>
                    <div className="column">
                        <DataCell label="PM2.5" value={weatherData.pm2_5} measure="&#956;g/m&#179;" />
                        <DataCell label="PM10" value={weatherData.pm10} measure="&#956;g/m&#179;" />
                    </div>
                </div>
                <div className="forecast" id="forecast">
                  {weatherData.days.map((day, index) => (
                    <div className="block" key={index}>
                      <h3 className="secondary">{day.date}</h3>
                      {getWeatherIcon(day.condition)}
                      <h4 className="secondary">{aqIndex === "epa" ? (day.usEpaIndex ? day.usEpaIndex : "N/A") : (day.gbDefraIndex ? day.gbDefraIndex : "N/A")}</h4>
                    </div>
                  ))}
                </div>
            </div>
        </div>
    );
  }

export default ResultPanel;