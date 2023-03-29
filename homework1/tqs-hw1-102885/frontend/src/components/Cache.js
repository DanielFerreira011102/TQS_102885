import React, { useState, useEffect } from "react"
import CachePanel from "./CachePanel";
import ErrorPanel from "./ErrorPanel";
import LoadingPanel from "./LoadingPanel";
import './cache.css'

function Cache() {
  const [loading, setLoading] = useState(true);
  const [success, setSuccess] = useState(false);
  const [cacheData, setCacheData] = useState(null);
  const API_URL = '/api/v1/cache';

  useEffect(() => {
      fetch(API_URL)
          .then((response) => response.json())
          .then((data) => {
            setCacheData(data)
            setSuccess(true);
            setLoading(false);
          })
          .catch((error) => {
            console.error(error);
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
        <div></div>
      )
  );
}

export default Cache;