import React from "react"
import { SiDatabricks } from "react-icons/si";

function CachePanel({cacheData}) {

    return (
        <div className="panel__cache">

            <div className="title">Cache status</div>

            <div className="icon">
                <SiDatabricks />
            </div>

            <div className="features">
            <ul>
                <li><span>{cacheData.request_count}</span> Requests</li>
                <li><span>{cacheData.cache_hits}</span> Hits</li>
                <li><span>{cacheData.cache_misses}</span> Misses</li>
                <li><span>{cacheData.expired_count}</span> Expired</li>
            </ul>
            </div>
        </div>
    );
  }

export default CachePanel;