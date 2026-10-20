import { useState } from 'react'
import heroImg from './assets/hero.png'
import reactLogo from './assets/react.svg'
import viteLogo from './assets/vite.svg'
import './App.css'
import Navbar from './components/Navbar'

function App() {
  return(
    <div className="App">
      <Navbar />
      <p>Welcome to the Bus Booking System</p>
      <h1> Launching Soon Stay tuned ... </h1>
      <h2> - Bus Booking System</h2>
    </div>
  );
}

export default App
