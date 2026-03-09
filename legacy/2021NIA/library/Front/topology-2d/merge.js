const fs = require('fs');
const path = require('path');

// 병합할 파일 순서 정의
const filesToMerge = [
    './js/d3.v4.js',      // d3 라이브러리
    './js/d3-tip.js',     // d3-tip 라이브러리
    './js/dat.gui.min.js',// dat.gui 라이브러리
    './index_nia.js'      // index_nia.js (필수 포함)
];

// 병합된 파일 경로
const outputFilePath = './dist/index_nia_bundle.js';

// 병합 작업
const mergeFiles = (fileList, output) => {
    let mergedContent = '';

    fileList.forEach(file => {
        if (fs.existsSync(file)) {
            const fileContent = fs.readFileSync(file, 'utf-8');
            mergedContent += `\n// --- ${path.basename(file)} ---\n\n${fileContent}\n`;
        } else {
            console.error(`Error: File not found - ${file}`);
        }
    });

    // 병합된 파일 생성
    fs.writeFileSync(output, mergedContent, 'utf-8');
    console.log(`Successfully merged files into: ${output}`);
};

// 병합 실행
mergeFiles(filesToMerge, outputFilePath);