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
    customBorders: true,
    licenseKey: 'non-commercial-and-evaluation',
    afterChange: (changes) => {
        console.log(changes);
        //     changes.forEach(([row, prop, oldValue, newValue]) => {
        //         // Some logic...
        //     });
    },
    afterSelection: (row, column, row2, column2, preventScrolling, selectionLayerLevel) => {
        sendMessage("/ctrl/table/select", {top: row, left: column, bottom: row2, right: column2, select: true});
    },
    afterDeselect: () => {
        sendMessage("/ctrl/table/select", {top: -1, left: -1, bottom: -1, right: -1, select: false});
    }
});

function doTableLoad(loadData) {
    var data = [];
    loadData.rows.forEach((v, i) => {
        data.push([v.artist, v.title, v.isrc]);
    });
    data.push(["", "", ""]);
    hot.loadData(data);

    loadData.sessions.forEach((s) => {
        doTableSelect(s);
    });
}

const customBordersPlugin = hot.getPlugin('customBorders');
const orangeSelection = {
    left: {width: 2, color: 'orange'},
    right: {width: 2, color: 'orange'},
    top: {width: 2, color: 'orange'},
    bottom: {width: 2, color: 'orange'}
};

function doTableSelect(s) {
    if (s.uid === uid) {
        return
    }
    customBordersPlugin.clearBorders();
    customBordersPlugin.setBorders([
        [s.top, s.left, s.bottom, s.right]
    ], orangeSelection);
}
