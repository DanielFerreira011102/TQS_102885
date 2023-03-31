import React, { useState, useEffect } from "react"
import { useParams } from 'react-router-dom';
import { getCode } from 'iso-3166-1-alpha-2';
import "./result.css"
import ResultPanel from "./ResultPanel";
import ErrorPanel from "./ErrorPanel";
import LoadingPanel from "./LoadingPanel";

const formatLocaltime = (localTime, short = false) => {
  const date = new Date(localTime);
  const options = short? { weekday: 'short' }: { weekday: 'long', hour: 'numeric', minute: 'numeric' };
  return date.toLocaleString('en-US', options);
};

function filterWeatherData(data) {
  const subarray = data.slice(1);
  return subarray.map(({ condition, air_quality, date }) => ({
    date: formatLocaltime(date, true),
    condition,
    usEpaIndex: air_quality["us-epa-index"],
    gbDefraIndex: air_quality["gb-defra-index"],
  }));
}

function Result() {
    const { query } = useParams();
    const API_URL = `/api/v1/forecast?location=${query}&days=6&current=true`
    const [weatherData, setWeatherData] = useState({});
    const [loading, setLoading] = useState(true);
    const [success, setSuccess] = useState(false);
    const [error, setError] = useState(null);
    
    useEffect(() => {
        fetch(API_URL)
        .then((response) => {
          if (!response.ok) {
            throw new Error(`ERROR ${response.status}: ${response.statusText}`);
          }
          return response.json();
        })
        .then((data) => {
            console.log(data)
            const {location, current, days} = data;
            const {air_quality, last_updated, condition, is_day } = current;
            const countryCode = getCode(location.country);
            const { co, no2, o3, so2, pm2_5, pm10, "us-epa-index": usEpaIndex, "gb-defra-index": gbDefraIndex } = air_quality;
            const formattedData = {
                name: location.name,
                country: countryCode,
                co: co.toFixed(2),
                no2: no2.toFixed(2),
                o3: o3.toFixed(2),
                so2: so2.toFixed(2),
                pm2_5: pm2_5.toFixed(2),
                pm10: pm10.toFixed(2),
                usEpaIndex,
                gbDefraIndex,
                condition,
                lastUpdated: last_updated,
                localtime: formatLocaltime(location.localtime),
                isDay: is_day,
                days: filterWeatherData(days),
            };
            setWeatherData(formattedData);
            setSuccess(true);
            setLoading(false);
        })
        .catch((error) => {
          setError(error)
          setSuccess(false);
          setLoading(false);
        });
    }, [API_URL]);

    return (
      loading?
        <LoadingPanel />
        :
        success? 
          (
            <ResultPanel  weatherData={weatherData} />
          )
          :
          (
            <ErrorPanel error={error} />
          )
    );
}

export default Result;