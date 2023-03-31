import React from "react"
import './error.css'

function ErrorPanel({query, error}) {

  console.log(error)

  return (
        <div className="panel__error">
          <span>{error.message}</span>
        </div>
    );
  }

export default ErrorPanel;