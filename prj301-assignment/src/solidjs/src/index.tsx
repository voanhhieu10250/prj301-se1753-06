/* @refresh reload */
import { render } from "solid-js/web";
import "swiper/css";
import "swiper/css/navigation";
import "swiper/css/effect-creative";
import "swiper/css/keyboard";
import "./index.css";
import App from "./App";

const root = document.getElementById("root");

if (import.meta.env.DEV && !(root instanceof HTMLElement)) {
  throw new Error(
    "Root element not found. Did you forget to add it to your index.html? Or maybe the id attribute got mispelled?"
  );
}

render(
  () => (
    <div class="App">
      <App />
    </div>
  ),
  root!
);
