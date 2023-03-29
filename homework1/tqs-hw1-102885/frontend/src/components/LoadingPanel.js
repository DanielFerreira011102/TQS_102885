import React from "react"
import { ImSpinner2 } from 'react-icons/im';

function LoadingPanel() {

    return (
        <ImSpinner2 className="loader" color='#fff' fontSize={64} />
    );
  }

export default LoadingPanel;