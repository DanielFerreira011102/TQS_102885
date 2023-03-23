import logo from './logo.svg';
import './App.css';
import {useEffect, useState} from "react";

function App() {
    const [clients, setClients] = useState(null);

    useEffect(() => {
        fetch('/clients')
            .then((response) => response.json())
            .then((data) => {console.log(data);setClients(data);});
    }, []);

    return (
        <div className="App">
            <header className="App-header">
                <img src={logo} className="App-logo" alt="logo" />
                <div className="App-intro">
                    <h2>Clients</h2>
                    {clients ? (
                        clients.map((client) => (
                            <div key={client.id}>
                                {client.name} ({client.email})
                            </div>
                        ))
                    ) : (
                        <div>Loading clients...</div>
                    )}
                </div>
            </header>
        </div>
    );
}

export default App;
