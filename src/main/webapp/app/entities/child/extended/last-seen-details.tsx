import React from 'react';
import { TextFormat } from 'react-jhipster';
import { Row, Col, Table } from 'reactstrap';
import { APP_DATE_FORMAT } from 'app/config/constants';

const LastSeenDetails = props => {
  const { lastSeens } = props;
  return (
    <Row className="justify-content-center">
      <Col md="12">
        <Table responsive className="table">
          <thead>
            <tr>
              <th>Street</th>
              <th>City</th>
              <th>State</th>
              <th>Country</th>
              <th>Date</th>
            </tr>
          </thead>
          <tbody>
            {lastSeens &&
              lastSeens.map((lastSeen, i) => (
                <tr key={`entity-${i}`}>
                  <td>{lastSeen.lastSeenAddress.street}</td>
                  <td>{lastSeen.lastSeenAddress.city}</td>
                  <td>{lastSeen.lastSeenAddress.state}</td>
                  <td>{lastSeen.lastSeenAddress.country}</td>
                  <td>
                    <TextFormat value={lastSeen.date} type="date" format={APP_DATE_FORMAT} />
                  </td>
                </tr>
              ))}
          </tbody>
        </Table>
      </Col>
    </Row>
  );
};

export default LastSeenDetails;
