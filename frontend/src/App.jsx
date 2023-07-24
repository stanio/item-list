import React, { useEffect, useState } from 'react';
import './App.css';

export default function App() {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setLoading(true);

    fetch('/item-list/api/items')
      .then(response => {
        if (response.status === 401) {
            // Reloading the top-level page should trigger redirect to login.
            window.location.reload();
            return [];
        }
        return response.json();
      })
      .then(data => {
        setItems(data);
        setLoading(false);
      });
  }, []);

  if (loading) {
    return <p>Loading...</p>;
  }

  return (
    <div className="App">
      <header className="App-header">
        <img src="/item-list/logo.svg" className="App-logo" alt="logo" />
        Item List
      </header>
      <main>
        <h1>Item List</h1>
        {items.length ? items.map(it =>
          <div key={it.id}>
            {it.label}
          </div>
        ) : (
          <p>(No items.)</p>
        )}
      </main>
    </div>
  );
}
