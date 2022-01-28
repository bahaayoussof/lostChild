import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './last-seen-address.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const LastSeenAddressDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const lastSeenAddressEntity = useAppSelector(state => state.lastSeenAddress.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="lastSeenAddressDetailsHeading">
          <Translate contentKey="lostChildApp.lastSeenAddress.detail.title">LastSeenAddress</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{lastSeenAddressEntity.id}</dd>
          <dt>
            <span id="street">
              <Translate contentKey="lostChildApp.lastSeenAddress.street">Street</Translate>
            </span>
          </dt>
          <dd>{lastSeenAddressEntity.street}</dd>
          <dt>
            <span id="city">
              <Translate contentKey="lostChildApp.lastSeenAddress.city">City</Translate>
            </span>
          </dt>
          <dd>{lastSeenAddressEntity.city}</dd>
          <dt>
            <span id="state">
              <Translate contentKey="lostChildApp.lastSeenAddress.state">State</Translate>
            </span>
          </dt>
          <dd>{lastSeenAddressEntity.state}</dd>
          <dt>
            <span id="country">
              <Translate contentKey="lostChildApp.lastSeenAddress.country">Country</Translate>
            </span>
          </dt>
          <dd>{lastSeenAddressEntity.country}</dd>
        </dl>
        <Button tag={Link} to="/last-seen-address" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/last-seen-address/${lastSeenAddressEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LastSeenAddressDetail;
