import React, { useState } from "react"
import "./search.css"

function Search() {
    const [query, setQuery] = useState('');
  
    const handleChange = (event) => {
      setQuery(event.target.value);
    };
  
    const handleSubmit = (event) => {
      event.preventDefault();
      window.location.href = `/${query}`;
    };
  
    return (
      <div className="search__wrapper">
        <form onSubmit={handleSubmit}>
            <input autoFocus id="search" className="search__input" name="search" type="text" value={query} placeholder="Search" onChange={handleChange} />
        </form>
      </div>
    );
  }

export default Search;