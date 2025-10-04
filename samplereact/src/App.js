import { useEffect, useState } from 'react';
import './App.css';

function App() {
  console.log("App loaded")
  const [counter, setCounter]= useState(() => ()=>
    {
      console.log("counter set");
      return 0;
    }
    )
  return (
    <div className="App" >
      Hello world
      <h1 onClick={()=> {
        setCounter(counter+1);
        setCounter((value)=>value+3);
      }
      }>{counter} </h1>
    {console.log("App render")}
    </div>
  );
}

export default App;
