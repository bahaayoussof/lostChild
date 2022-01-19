import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IAddress } from 'app/shared/model/address.model';
import { ILastSeen } from 'app/shared/model/last-seen.model';
import React, { useState } from 'react';
import { ValidatedField, translate, Translate } from 'react-jhipster';
import { Row, Col, Button, Table } from 'reactstrap';
import './child.scss';
/* eslint-disable no-console */
const ChildLastSeen = props => {
  const { childLastSeens } = props;
  const [lastSeens, setLastSeens] = useState(childLastSeens ?? []);
  const [lastSeen, setLastSeen] = useState({} as ILastSeen);
  const [address, setAddress] = useState({} as IAddress);

  const handleDate = target => {
    setLastSeen({ ...lastSeen, date: target.value });
  };

  const handleStreet = target => {
    const street = target.value;
    setAddress({ ...address, street });
  };

  const handleCity = target => {
    const city = target.value;
    setAddress({ ...address, city });
  };

  const handleState = target => {
    const state = target.value;
    setAddress({ ...address, state });
  };

  const handleCountry = target => {
    const country = target.value;
    setAddress({ ...address, country });
  };

  const handleAddLastSeen = () => {
    lastSeen.address = address;
    setLastSeens([...lastSeens, lastSeen]);
    props.handleLastSeens(lastSeen);
    setAddress({});
    setLastSeen({});
  };

  return (
    <>
      <Row className="justify-content-center">
        <Row>
          <h5 className="header">Last Seen</h5>
          <Col md={6}>
            <ValidatedField
              placeholder={translate('lostChildApp.address.street')}
              id="address-street"
              name="lastSeen.address.street"
              data-cy="street"
              type="text"
              value={address.street ?? ''}
              validate={{
                required: { value: true, message: translate('entity.validation.required') },
              }}
              onChange={event => handleStreet(event.target)}
            />
            <ValidatedField
              placeholder={translate('lostChildApp.address.city')}
              id="address-city"
              name="lastSeen.address.city"
              data-cy="city"
              type="text"
              value={address.city ?? ''}
              validate={{
                required: { value: true, message: translate('entity.validation.required') },
              }}
              onChange={event => handleCity(event.target)}
            />
          </Col>
          <Col md={6}>
            <ValidatedField
              placeholder={translate('lostChildApp.address.state')}
              id="address-state"
              name="lastSeen.address.state"
              data-cy="state"
              type="text"
              value={address.state ?? ''}
              onChange={event => handleState(event.target)}
            />
            <ValidatedField
              placeholder={translate('lostChildApp.address.country')}
              id="address-country"
              name="lastSeen.address.country"
              data-cy="country"
              type="text"
              value={address.country ?? ''}
              onChange={event => handleCountry(event.target)}
            />
          </Col>
        </Row>
        <Row>
          <Col md={6}>
            <ValidatedField
              placeholder={translate('lostChildApp.lastSeen.date')}
              id="last-seen-date"
              name="lastSeen.date"
              data-cy="date"
              type="date"
              value={lastSeen.date ?? ''}
              onInput={event => handleDate(event.target)}
              validate={{
                required: { value: true, message: translate('entity.validation.required') },
              }}
            />
          </Col>
          <Col md={6}>
            <Button id="saveLastSeen" color="success" onClick={handleAddLastSeen} disabled={!lastSeen.date}>
              Add Last Seen!
            </Button>
          </Col>
        </Row>
      </Row>

      <Row className="justify-content-center">
        <Col md="12">
          <Table responsive className="table">
            <thead>
              <tr>
                <th>Date</th>
                <th>Street</th>
                <th>City</th>
                <th>State</th>
                <th>Country</th>
              </tr>
            </thead>
            <tbody>
              {lastSeens &&
                lastSeens.map((ls, i) => (
                  <tr key={`entity-${i}`}>
                    <td>{ls.date}</td>
                    <td>{ls.address.street}</td>
                    <td>{ls.address.city}</td>
                    <td>{ls.address.state}</td>
                    <td>{ls.address.country}</td>
                  </tr>
                ))}
            </tbody>
          </Table>
        </Col>
      </Row>
    </>
  );
};

export default ChildLastSeen;
