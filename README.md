# ToDo-App
Academic project for UBC CPSC 210, Fall Sem 2 2019

### Summary
A productivity app for creating projects and tasks with: 
- description (name)
- due-dates
- Eisenhower Decision Matrix priority
- status
- estimated time to completion
- tags

## Features, Implementations
- add, remove, edit
- create and parse JSON data to load/save tasks
- composite design pattern: project is a composite, task is a leaf, both extend a todo component
- observer design pattern: todo (the component) is an observable, project is an observer; changing the estimated time of completion in any way updates a project's etcHours field directly 
- iterator design pattern: can iterate through each todo contained in a project
- JUnit testing

