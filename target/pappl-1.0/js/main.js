/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
function getTarget(ev) {
  var target = ev.target;
  while ((target !== null) && (target !== undefined) && (target.id.substring(0, 4) !== "list")) {
    target = target.parentNode;
  }
  return target;
}

function allowDrop(ev) {
  ev.preventDefault();
}

function drag(ev) {
  ev.dataTransfer.setData("dragelt", ev.target.id);
}

function drop(ev) {
  ev.preventDefault();
  var data = ev.dataTransfer.getData("dragelt");
  var dragElt = document.getElementById(data);
  var target = getTarget(ev);
  if ((target !== null) && (target !== undefined) && (dragElt !== null)) {
    target.appendChild(dragElt);
  }
}

function dropAndRename(ev, oldName, newName) {
  ev.preventDefault();
  var data = ev.dataTransfer.getData("dragelt");
  var dragElt = document.getElementById(data);
  var target = getTarget(ev);
  if ((target !== null) && (target !== undefined) && (dragElt !== null)) {
    target.appendChild(dragElt);

    replaceElements(target, oldName, newName);
  }
}

function filterList(theListID, theInputId) {
  if ((theListID !== null) && (theListID !== undefined) && (theInputId !== null) && (theInputId !== undefined)) {
    var input = document.getElementById(theInputId);
    var list = document.getElementById(theListID);
    if ((input !== null) && (input !== undefined) && (list !== null) && (list !== undefined)) {
      var filter = input.value.toUpperCase();
      var elements = list.childNodes;
      for (var i = 0; i < elements.length; i++) {
        var element = elements[i];
        if (element.nodeType === Node.ELEMENT_NODE) {
          if (element.tagName === "DIV") {
            var elementContent = getTextChild(element);
            if (elementContent.toUpperCase().indexOf(filter) > -1) {
              element.style.display = "";
            } else {
              element.style.display = "none";
            }
          }
        }
      }
    }
  }
}