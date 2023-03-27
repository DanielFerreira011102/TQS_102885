import React, { useState, useEffect } from "react"

function Result() {
  
    const [clients, setClients] = useState(null);

    useEffect(() => {
        fetch('/clients')
            .then((response) => response.json())
            .then((data) => {console.log(data);setClients(data);});
    }, []);

    return (
      <div>
        <h1>Cache!</h1>
      </div>
    );
}

export default Result;