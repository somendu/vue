import React, { useState } from 'react';

import Table from 'react-bootstrap/Table'
import Button from 'react-bootstrap/Button'

const TableShowcase: React.FC = () => {
    const [show, toggleShow] = useState(true);

    return <>
    {!show && <Button onClick={() => toggleShow(true)}>Show Toast</Button>}
    {/*
    // @ts-ignore */}
    <Table show={show} onClose={() => toggleShow(false)}>
        <tr>
            <img src="holder.js/20x20?text=%20" className="rounded mr-2" alt="" />
            <strong className="mr-auto">Bootstrap</strong>
            <small>11 mins ago</small>
        </tr>
 
    </Table> 
</>
}

export default TableShowcase


