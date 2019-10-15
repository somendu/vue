
import React, { useState } from 'react';


import Table from 'react-bootstrap/Table'
import Button from 'react-bootstrap/Button'

const TableShowcase: React.FC = () => {
    const [show, toggleShow] = useState(true);
    var text = "This is to Show";

    return <>
    {!show && <Button onClick={() => toggleShow(true)}>Show Toast</Button>}
    {/*
    // @ts-ignore */}
    <Table show={show} onClose={() => toggleShow(false)}>
        <tr>
            <img src="holder.js/20x20?text=%20" className="rounded mr-2" alt="" />
            <strong className="mr-auto">Bootstrap</strong>
            <small>{text}</small>
        </tr>
 
    </Table> 
</>
}

export default TableShowcase


