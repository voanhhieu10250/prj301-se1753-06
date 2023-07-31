# SolidJS components

This folder contains all of the SolidJS components that are used by the java project.

---

## Usage

```bash
$ npm install # or pnpm install or yarn install
```

---

## Available Scripts

In the project directory, you can run:

### **Development mode**.

`npm dev` or `npm start`

Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

The page will reload if you make edits.<br>

During dev, if you want to create a new component (to use in jsp):

- Create a html file with the following contents:

```html
<div id="root"></div>

<script src="{path.to.your.solid.script}.tsx" type="module"></script>
```

Then simply navigate to the html file you just create - it works as expected, just like for a normal static file server.

### **Production mode**

`npm run build`
Builds the app for production to the `dist` folder.<br/>
Or you can build the app in the `webapp` folder of your java project by change the ourDir in `vite.config.js`<br />

**_Your components are ready to be used with JSP files! Copy the built files to webapp folder and reference them in your JSP files._**

It correctly bundles Solid in production mode and optimizes the build for the
best performance. The build is minified and the filenames include the hashes.<br />
