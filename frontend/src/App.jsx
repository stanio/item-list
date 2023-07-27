import './App.css';
import { Navbar, NavbarBrand } from 'reactstrap';
import ItemList from './ItemList';

export default function App() {
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
        <ItemList />
      </main>
    </>
  );
}
