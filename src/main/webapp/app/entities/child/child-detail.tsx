import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './child.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ChildDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const childEntity = useAppSelector(state => state.child.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="childDetailsHeading">
          <Translate contentKey="lostChildApp.child.detail.title">Child</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{childEntity.id}</dd>
          <dt>
            <span id="image">
              <Translate contentKey="lostChildApp.child.image">Image</Translate>
            </span>
          </dt>
          <dd>
            {childEntity.image ? (
              <div>
                {childEntity.imageContentType ? (
                  <a onClick={openFile(childEntity.imageContentType, childEntity.image)}>
                    <img src={`data:${childEntity.imageContentType};base64,${childEntity.image}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {childEntity.imageContentType}, {byteSize(childEntity.image)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="name">
              <Translate contentKey="lostChildApp.child.name">Name</Translate>
            </span>
          </dt>
          <dd>{childEntity.name}</dd>
          <dt>
            <span id="age">
              <Translate contentKey="lostChildApp.child.age">Age</Translate>
            </span>
          </dt>
          <dd>{childEntity.age}</dd>
          <dt>
            <span id="gender">
              <Translate contentKey="lostChildApp.child.gender">Gender</Translate>
            </span>
          </dt>
          <dd>{childEntity.gender}</dd>
          <dt>
            <span id="birthdate">
              <Translate contentKey="lostChildApp.child.birthdate">Birthdate</Translate>
            </span>
          </dt>
          <dd>{childEntity.birthdate ? <TextFormat value={childEntity.birthdate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="lostChildApp.child.status">Status</Translate>
            </span>
          </dt>
          <dd>{childEntity.status}</dd>
          <dt>
            <span id="agency">
              <Translate contentKey="lostChildApp.child.agency">Agency</Translate>
            </span>
          </dt>
          <dd>{childEntity.agency}</dd>
          <dt>
            <Translate contentKey="lostChildApp.child.address">Address</Translate>
          </dt>
          <dd>{childEntity.address ? childEntity.address.id : ''}</dd>
          <dt>
            <Translate contentKey="lostChildApp.child.description">Description</Translate>
          </dt>
          <dd>{childEntity.description ? childEntity.description.id : ''}</dd>
          <dt>
            <Translate contentKey="lostChildApp.child.contactInformation">Contact Information</Translate>
          </dt>
          <dd>{childEntity.contactInformation ? childEntity.contactInformation.id : ''}</dd>
          <dt>
            <Translate contentKey="lostChildApp.child.user">User</Translate>
          </dt>
          <dd>{childEntity.user ? childEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/child" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/child/${childEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ChildDetail;
