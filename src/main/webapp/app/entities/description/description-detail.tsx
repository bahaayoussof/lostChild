import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './description.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const DescriptionDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const descriptionEntity = useAppSelector(state => state.description.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="descriptionDetailsHeading">
          <Translate contentKey="lostChildApp.description.detail.title">Description</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{descriptionEntity.id}</dd>
          <dt>
            <span id="eyeColor">
              <Translate contentKey="lostChildApp.description.eyeColor">Eye Color</Translate>
            </span>
          </dt>
          <dd>{descriptionEntity.eyeColor}</dd>
          <dt>
            <span id="hairColor">
              <Translate contentKey="lostChildApp.description.hairColor">Hair Color</Translate>
            </span>
          </dt>
          <dd>{descriptionEntity.hairColor}</dd>
          <dt>
            <span id="weight">
              <Translate contentKey="lostChildApp.description.weight">Weight</Translate>
            </span>
          </dt>
          <dd>{descriptionEntity.weight}</dd>
          <dt>
            <span id="height">
              <Translate contentKey="lostChildApp.description.height">Height</Translate>
            </span>
          </dt>
          <dd>{descriptionEntity.height}</dd>
        </dl>
        <Button tag={Link} to="/description" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/description/${descriptionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DescriptionDetail;
