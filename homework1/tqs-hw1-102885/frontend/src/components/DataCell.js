import React from "react"

function DataCell({label, value, measure}) {
    return (
        <h3 className="cell">
            <span className="label">{label}: </span>
            <span className="value">{value} {measure}</span>
        </h3>
    );
  }

export default DataCell;