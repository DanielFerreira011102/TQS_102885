import React, { useState, useEffect } from "react"
import CachePanel from "./CachePanel";
import ErrorPanel from "./ErrorPanel";
import LoadingPanel from "./LoadingPanel";
import './cache.css'

function Cache() {
  const API_URL = '/api/v1/cache';
  const [loading, setLoading] = useState(true);
  const [success, setSuccess] = useState(false);
  const [cacheData, setCacheData] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
      fetch(API_URL)
          .then((response) => {
            if (!response.ok) {
              throw new Error(response.statusText);
            }
            return response.json();
          })
          .then((data) => {
            setCacheData(data)
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
        <CachePanel cacheData={cacheData} />
      )
      :
      (
        <ErrorPanel error={error} />
      )
  );
}

export default Cache;