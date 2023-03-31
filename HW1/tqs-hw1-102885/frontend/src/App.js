import './App.css';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import { Search, Result, Cache } from "./components"
import { AiOutlineFileSearch, AiOutlineDatabase } from "react-icons/ai";

function App() {
  return (
    <div className='root__wrapper'>
        <Router>
        <div className='main__wrapper'>
            <nav className='navbar__wrapper'>
                <ul>
                    <li>
                        <Link to="/"><AiOutlineFileSearch color='#fff' fontSize={48}/></Link>
                    </li>
                    <li>
                        <Link to="/cache"><AiOutlineDatabase color='#fff' fontSize={48}/></Link>
                    </li>
                </ul>
            </nav>

            <Routes>
                <Route path="/" element={<Search />} />
                <Route path="/:query" element={<Result />} />
                <Route path="/cache" element={<Cache />} />
            </Routes>
        </div>
        </Router>
    </div>
  );
}
export default App;
