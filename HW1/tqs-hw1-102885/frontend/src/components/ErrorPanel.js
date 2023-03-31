import React, { useEffect } from "react"
import './error.css'

function ErrorPanel({error}) {

    return (
        <div className="panel__error">
            <span>{error.message}</span>
        </div>
    );
}

export default ErrorPanel;