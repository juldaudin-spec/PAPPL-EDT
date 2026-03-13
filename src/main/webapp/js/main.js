/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
function getTarget(ev) {
  // plus robuste que la boucle manuelle
  return ev.target.closest('[id^="list"]');
}

function allowDrop(ev) { ev.preventDefault(); }

function drag(ev) {
  ev.dataTransfer.setData("text/plain", ev.currentTarget.id);
}

function dropAndRename(ev, oldPrefix, newPrefix) {
  ev.preventDefault();
  const data = ev.dataTransfer.getData("text/plain");
  const dragElt = document.getElementById(data);
  const target = getTarget(ev);

  if (target && dragElt) {
    target.appendChild(dragElt);
    renameDraggedItem(dragElt, oldPrefix, newPrefix);
  }
}

function renameDraggedItem(dragElt, oldPrefix, newPrefix) {
  // rename div id: ml_X -> m_X etc.
  if (dragElt.id && dragElt.id.startsWith(oldPrefix + "_")) {
    dragElt.id = newPrefix + dragElt.id.substring(oldPrefix.length);
  }

  // rename hidden input name: ml[X] -> m[X] etc.
  const hidden = dragElt.querySelector('input[type="hidden"]');
  if (hidden && hidden.name && hidden.name.startsWith(oldPrefix + "[")) {
    hidden.name = newPrefix + hidden.name.substring(oldPrefix.length);
  }
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

function filterList(theListID, theInputId) {
  const input = document.getElementById(theInputId);
  const list = document.getElementById(theListID);
  if (!input || !list) return;

  const filter = (input.value || "").toUpperCase();

  // children = uniquement les éléments (pas les text nodes)
  for (const element of list.children) {
    const txt = (element.textContent || "").toUpperCase();
    element.style.display = txt.includes(filter) ? "" : "none";
  }
}
