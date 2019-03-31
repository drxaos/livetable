var data = [
    ['', '', ''],
];

var container = document.getElementById('hot');
var hot = new Handsontable(container, {
    data: data,
    rowHeaders: true,
    colHeaders: ['Artist', 'Title', 'ISRC'],
    filters: true,
    dropdownMenu: false,
    licenseKey: 'non-commercial-and-evaluation'
});

function doTableLoad(loadData) {
    var data = [];
    loadData.rows.forEach((v, i) => {
        data.push([v.artist, v.title, v.isrc]);
    });
    data.push(["", "", ""]);
    hot.loadData(data);
}