import React, { useEffect, useState } from 'react';
import './App.css';
import { Navbar, NavbarBrand, Table } from 'reactstrap';

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

  const captionRow = {
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
    margin: '0 7px'
  };

  return (
    <>
      <Navbar tag="header" color="secondary" expand="md">
        <NavbarBrand href="/item-list/">
          <img src="/item-list/logo.svg" className="App-logo" alt="logo" />
          {' '}
          Item List
	</NavbarBrand>
      </Navbar>
      <main>
        <Table dark responsive striped className="caption-top">
          <caption>
            <div style={captionRow}>
              <span style={{flex: 1}}>Items</span>
              {' '}
              <button className="btn btn-success float-end" title="New">
                <span aria-hidden="true">&#x2795;&#xFE0E;</span>
              </button>
            </div>
          </caption>
          {items.length ? <ItemRows items={items} />
                        : <tbody>
                            <tr>
                              <td className="text-center">
                                (No items)
                              </td>
                            </tr>
                          </tbody>}
        </Table>
      </main>
    </>
  );
}

function ItemRows({ items }) {
  return (
    <>
      <thead>
        <tr>
          <th scope="col" className="w-100">Description</th>
          <th scope="col">Action</th>
        </tr>
      </thead>
      <tbody>
        {items.map(it =>
          <tr key={it.id}>
            <td className="w-100">{it.label}</td>
            <td>
              <div className="btn-group">
                <button type="button" className="btn btn-primary" title="Edit">
                  <span aria-hidden="true">&#x1F589;&#xFE0E;</span>
                </button>
                <button type="button" className="btn btn-danger" title="Delete">
                  <span aria-hidden="true">&#x1F5D9;&#xFE0E;</span>
                </button>
              </div>
            </td>
          </tr>
        )}
      </tbody>
    </>
  );
}
