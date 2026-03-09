var fs = require('fs');
 
fs.readFile('../../../../demo/index_dev.js', 'utf-8', function(err, data) {
    if (err) throw err;
 
    var newValue = data.replace(/filePath\:.*/g, 'filePath: \'data/\',')
		.replace(/fileName\:.*/g, 'fileName: \'data_nia_1.json\',')
		.replace(/link_type\:.*/g, 'link_type: \'line\',')
		.replace(/window\.onload \= \(\) \=\> \{/g, 'window.onload = () => { return;')
 
    fs.writeFile('index_dev.js', newValue, 'utf-8', function(err, data) {
        if (err) throw err;
        console.log('Done!');
    })
})
