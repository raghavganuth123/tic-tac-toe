const board = document.getElementById('board');
const cells = document.querySelectorAll('[data-cell]');
const restartBtn = document.getElementById('restartBtn');
const toggleModeBtn = document.getElementById('toggleModeBtn');
const statusMessage = document.getElementById('statusMessage');
let isPlayerXTurn = true;
let gameState = Array(9).fill(null);
let aiMode = false;

const WINNING_COMBINATIONS = [
    [0, 1, 2],
    [3, 4, 5],
    [6, 7, 8],
    [0, 3, 6],
    [1, 4, 7],
    [2, 5, 8],
    [0, 4, 8],
    [2, 4, 6]
];

cells.forEach(cell => {
    cell.addEventListener('click', handleClick, { once: true });
});

restartBtn.addEventListener('click', restartGame);
toggleModeBtn.addEventListener('click', toggleMode);

function handleClick(e) {
    const cell = e.target;
    const currentClass = isPlayerXTurn ? 'X' : 'O';
    placeMark(cell, currentClass);
    if (checkWin(currentClass)) {
        endGame(false, currentClass);
    } else if (isDraw()) {
        endGame(true);
    } else {
        swapTurns();
        if (aiMode && !isPlayerXTurn) {
            aiMove();
        }
    }
}

function placeMark(cell, currentClass) {
    const cellIndex = Array.from(cells).indexOf(cell);
    gameState[cellIndex] = currentClass;
    cell.textContent = currentClass;
}

function swapTurns() {
    isPlayerXTurn = !isPlayerXTurn;
    setStatusMessage();
}

function setStatusMessage() {
    statusMessage.textContent = `Player ${isPlayerXTurn ? 'X' : 'O'}'s turn`;
}

function checkWin(currentClass) {
    return WINNING_COMBINATIONS.some(combination => {
        return combination.every(index => {
            return gameState[index] === currentClass;
        });
    });
}

function isDraw() {
    return gameState.every(cell => cell !== null);
}

function endGame(draw, winner = null) {
    if (draw) {
        statusMessage.textContent = 'Draw!';
    } else {
        statusMessage.textContent = `Player ${winner} wins!`;
    }
    cells.forEach(cell => {
        cell.removeEventListener('click', handleClick);
    });
}

function restartGame() {
    isPlayerXTurn = true;
    gameState = Array(9).fill(null);
    cells.forEach(cell => {
        cell.textContent = '';
        cell.addEventListener('click', handleClick, { once: true });
    });
    setStatusMessage();
}

function toggleMode() {
    aiMode = !aiMode;
    toggleModeBtn.textContent = aiMode ? 'Switch to 2 Player Mode' : 'Switch to AI Mode';
    restartGame();
}

function aiMove() {
    const emptyCells = gameState
        .map((cell, index) => (cell === null ? index : null))
        .filter(index => index !== null);
    const randomIndex = emptyCells[Math.floor(Math.random() * emptyCells.length)];
    const aiCell = cells[randomIndex];
    placeMark(aiCell, 'O');
    if (checkWin('O')) {
        endGame(false, 'O');
    } else if (isDraw()) {
        endGame(true);
    } else {
        swapTurns();
    }
}

setStatusMessage();
