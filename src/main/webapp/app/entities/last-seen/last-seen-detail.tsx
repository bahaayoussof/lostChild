import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './last-seen.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const LastSeenDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const lastSeenEntity = useAppSelector(state => state.lastSeen.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="lastSeenDetailsHeading">
          <Translate contentKey="lostChildApp.lastSeen.detail.title">LastSeen</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{lastSeenEntity.id}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="lostChildApp.lastSeen.date">Date</Translate>
            </span>
          </dt>
          <dd>{lastSeenEntity.date ? <TextFormat value={lastSeenEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="lostChildApp.lastSeen.address">Address</Translate>
          </dt>
          <dd>{lastSeenEntity.address ? lastSeenEntity.address.id : ''}</dd>
          <dt>
            <Translate contentKey="lostChildApp.lastSeen.child">Child</Translate>
          </dt>
          <dd>{lastSeenEntity.child ? lastSeenEntity.child.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/last-seen" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/last-seen/${lastSeenEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LastSeenDetail;
