# Welcome to front-end part

How to run & develop locally:


1.  Install `homebrew` if you don't have it with `/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
`
2. Install node.js if you don't have it `brew install node`
3. `cd fe`
4. type `npm -v`. If you receive version, that's correct
5. `npm install` to update all dependencies
6. `npm run start` to start server


Run with backend


1. Run backend
2. type in console ` ifconfig | grep "inet " | grep -v 127.0.0.1`
3. you'll receive `inet 192.168.1.83 netmask 0xffffff00 broadcast 192.168.1.255`
4. copy ip from`inet: xxx.xxx.xxx.xxx` and put it into `package.json` proxy field
5. re-run front end with `npm run start`


FYI: any operations with `npm` should be done from `project/fe` directory. All steps (except proxy step 4) should be done in console. If you'll receive bug 'something is running on port XXXX' just change port at `env` file

Short description:

As main framework I've used react. Main data flow organized through redux. Search request is asynchronous, redux-thunk is used for that. Responsible based on react-bootstrap, which allows support all mobile devices out-of-the box. I've tried to make stateless components and keep all data at store to provide scalability and replaceability. User statistic kept at local storage (in future we plan to sync up this data with BE but we hadn't time for that).
