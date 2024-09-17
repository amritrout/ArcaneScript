const editor = CodeMirror.fromTextArea(document.getElementById('codeEditor'), {
    lineNumbers: true,
    mode: 'text/x-java',
    theme: 'monokai',
    lineWrapping: true,
    viewportMargin: Infinity
});

editor.setValue(`//Write your code here
println "Hello, World!";
variable = 5;
for (i = 0; i < 10; i = i + 1;) {
  println i;
}`);

const outputElement = document.getElementById('output');
outputElement.textContent = "Press Run to get the output";

document.getElementById('runButton').addEventListener('click', async function() {
    const code = editor.getValue();
    console.log("Executing code:");
    console.log(code);

    outputElement.textContent = "Executing...\n";

    const formData = new FormData();
    const blob = new Blob([code], { type: 'text/plain' });
    formData.append('postData', blob, 'code.txt');

    try {
        const response = await fetch('http://localhost:8443/', {
            method: 'POST',
            body: formData
        });

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const result = await response.text();
        outputElement.textContent += "\n\nExecution complete!\n" + result;
    } catch (error) {
        console.error('Error:', error);
        outputElement.textContent += "\n\nFailed to execute code: " + error.message;
    }
});

document.getElementById('sidebarToggle').addEventListener('click', function() {
    const sidebar = document.getElementById('syntaxSidebar');
    const toggleButton = document.getElementById('sidebarToggle');

    sidebar.classList.toggle('collapsed');
    toggleButton.classList.toggle('collapsed');
});


document.querySelector('#output').style.border = '1px solid #ccc';
document.querySelector('#output').style.padding = '10px';
document.querySelector('#output').style.minHeight = '100px';
