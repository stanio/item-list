import React, { useEffect, useState } from 'react';
import { Button, ButtonGroup, Table } from 'reactstrap';
//import { Link } from 'react-router-dom';

export default function ItemList() {
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
      {items.length ? <ItemRows items={items} setItems={setItems} />
                    : <tbody>
                        <tr>
                          <td className="text-center">
                            (No items)
                          </td>
                        </tr>
                      </tbody>}
    </Table>
  );
}

function ItemRows({ items, setItems }) {

  async function deleteItem(id) {
    await fetch(`/item-list${id}`, {
      method: 'DELETE'
    }).then(() => {
      setItems([...items].filter(it => it.id !== id));
    });
  }

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
            <td className="w-100"
                style={{ whiteSpace: 'pre-line',
                         textOverflow: 'ellipsis'}}>{it.label}</td>
            <td>
              <ButtonGroup>
                <Button color="primary" title="Edit">
                  <span aria-hidden="true">&#x1F589;&#xFE0E;</span>
                </Button>
                <Button color="danger" onClick={() => deleteItem(it.id)} title="Delete">
                  <span aria-hidden="true">&#x1F5D9;&#xFE0E;</span>
                </Button>
              </ButtonGroup>
            </td>
          </tr>
        )}
      </tbody>
    </>
  );
}
