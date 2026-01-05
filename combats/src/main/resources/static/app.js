let socket;
let combatId = "";
let targetId = "";
let action = "";
let combatData;
let playing = false
let combatRunningData;
let player1 = "";

document.addEventListener('DOMContentLoaded', () =>
    document.getElementById("start-combat").onclick = e => runCombat(combatId, player1)
);

function attack() {
    socket.send();
}

function handleCombatMessage() {
    let combatMessage = JSON.parse(event.data);
    console.log(combatMessage);
    document.getElementById("action").innerHTML = combatMessage.msg || combatMessage.data.action
    combatData = combatMessage.data.combatData;
    clearTable('combatData');
    addCombatDataToTable(combatMessage.data.combatData, combatMessage.data.next)

}

function runCombat(id, idPlayer) {
    const serverURL = `ws://localhost:8081/ws/combat/${id}/${idPlayer}`;
    socket = new WebSocket(serverURL);
    socket.onopen = logOpenToConsole;
    socket.onclose = logCloseToConsole;
    socket.onmessage = handleCombatMessage;
}

async function createCombat() {
    const endpoint = "http://localhost:80/combat";
    try {
       player1 = document.getElementById("idPlayer1").value;
       const player2 = document.getElementById("idPlayer2").value;

        const response = await fetch(endpoint, {
            method: "POST",
            body: JSON.stringify({ player1Id: player1, player2Id: player2, isVsAi: true }),
        });

        if (!response.ok) {
          throw new Error(`Response status: ${response.status}`);
        }

        const result = await response.json();
        console.log(result);
        combatRunningData = result.data.combatCreatedData;
        combatId = combatRunningData.combat.id

        addCombatToTable(result.data.combatCreatedData.combat);
    } catch (error) {
        console.error(error.message);
    }
}

function addCombatToTable(combat) {
    tableBody("combat").appendChild(taskCombat(combat));
}

function taskCombat(combat) {
    return tr([
        td(combat.id),
        td(combat.idPlayer1),
        td(combat.idPlayer2),
    ]);
}

function taskCombatData(combatData, allCombatData, idNext) {
    return tr([
        td(combatData.id),
        td(combatData.idPlayer),
        td(combatData.name),
        td(combatData.creatureClass),
        td(combatData.level),
        td(combatData.xp),
        td(combatData.hp),
        td(combatData.speed),
        td(combatData.attack),
        td(combatData.defense),
        td(combatData.timeToAttack),
        tdSelect(combatRunningData.combat.player1Id || combatData.idPlayer, combatData.id, combatData.id !== idNext),
        tdSelectTarget(combatRunningData.combat.player1Id || combatData.idPlayer, combatData.id, allCombatData, combatData.id !== idNext),
        tdButtonAttack(combatData.id, combatData.id !== idNext)
    ]);
}

function addCombatDataToTable(combatData, idNext) {
    combatData.forEach(cd => tableBody("combatData").appendChild(taskCombatData(cd, combatData, idNext)));
}

function actionSelected(id) {
    socket.send(
        JSON.stringify({
          sourceId: id,
          targetId: targetId,
          action: action
        })
    )
}

function targetSelected(select) {
    targetId = select.value
}

function readAndDisplayAllTasks() {
    clearTable();

    const serverURL = 'ws://127.0.0.1:8080/tasks';
    const socket = new WebSocket(serverURL);

    socket.onopen = logOpenToConsole;
    socket.onclose = logCloseToConsole;
    socket.onmessage = readAndDisplayTask;
}

function readAndDisplayTask(event) {
    let task = JSON.parse(event.data);
    logTaskToConsole(task);
    addTaskToTable(task);
}

function logTaskToConsole(task) {
    console.log(`Received ${task.name}`);
}

function logCloseToConsole() {
    console.log("Web socket connection closed");
}

function logOpenToConsole() {
    console.log("Web socket connection opened");
}

function tableBody(id) {
    return document.getElementById(id);
}

function clearTable(id) {
    tableBody(id).innerHTML = "";
}

function addTaskToTable(task) {
    tableBody().appendChild(taskRow(task));
}


function taskRow(task) {
    return tr([
        td(task.name),
        td(task.description),
        td(task.priority)
    ]);
}


function tr(children) {
    const node = document.createElement("tr");
    children.forEach(child => node.appendChild(child));
    return node;
}


function td(text) {
    const node = document.createElement("td");
    node.appendChild(document.createTextNode(text));
    return node;
}

function tdSelect(idPlayer, id, disabled) {
    const select = document.createElement("select");
    select.innerHTML =
      '<options><option value=""></option><option value="CAUSED_DAMAGE">ATTACK</option><option value="DEFENSE_RECEIVED">DEFENSE</option></options>';
    select.onchange = ((control) => (e) => {
      action = control.value;
      updateSelectTarget(idPlayer, id, action)
    })(select)//actionSelected(select, id);
    select.disabled = disabled ? "disabled" : undefined;
    const node = document.createElement("td");
    //node.appendChild(document.createTextNode(text));
    node.appendChild(select);
    return node;
}

function updateSelectTarget(idPlayer, id, selectedAction) {
   const select = document.getElementById(`targets-${id}`);
   select.innerHTML = '<options><option value=""></option>'
   let dataForTarget = combatData.filter(c =>
     c.hp > 0
     && (
       (selectedAction === 'CAUSED_DAMAGE' && c.idPlayer != idPlayer)
       || ((selectedAction === 'DEFENSE_RECEIVED' && c.idPlayer === idPlayer))
     )
   );
   dataForTarget.forEach(cd => select.innerHTML += `<option value="${cd.id}">${cd.name}</option>`);
   select.innerHTML += '</options>'
   //select.onchange = (e) => targetSelected(select);
}

function tdSelectTarget(idPlayer, id, data, disabled) {
   const select = document.createElement("select");
   select.id = `targets-${id}`;
   select.innerHTML = '<options><option value=""></option>';

   data.forEach(combatData => select.innerHTML += `<option value="${combatData.id}">${combatData.name}</option>`);
   select.innerHTML += '</options>'
   select.onchange = (e) => targetSelected(select);
   select.disabled = disabled ? "disabled" : undefined;
   const node = document.createElement("td");
   node.appendChild(select);
   return node;
}

function tdButtonAttack(id, disabled) {
  const button = document.createElement("button");
  button.innerHTML = "PLAY"
  button.onclick = (e) => actionSelected(id);
  button.disabled = disabled ? "disabled" : undefined;
  const node = document.createElement("td");
  node.appendChild(button);
  return button;
}