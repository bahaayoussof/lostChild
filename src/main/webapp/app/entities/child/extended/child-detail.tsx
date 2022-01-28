import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Card, CardBody } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './child.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT, AUTHORITIES } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { hasAnyAuthority } from 'app/shared/auth/private-route';

export const ChildDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();
  const isAdmin = useAppSelector(state => hasAnyAuthority(state.authentication.account.authorities, [AUTHORITIES.ADMIN]));

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const childEntity = useAppSelector(state => state.child.entity);
  return (
    <div className="child-details">
      <Card className="details-container">
        <h1 className="missing">Missing</h1>
        <CardBody>
          <Row>
            <h3 className="child-name">{childEntity.name}</h3>
            <Col lg={6}>
              {childEntity.image ? (
                <div className="image-container">
                  {childEntity.imageContentType ? <img src={`data:${childEntity.imageContentType};base64,${childEntity.image}`} /> : null}
                </div>
              ) : null}
              <Row>
                <Col lg={6}>
                  <p>
                    <label>Birthday:</label>
                    {childEntity.birthdate ? <TextFormat value={childEntity.birthdate} type="date" format={APP_DATE_FORMAT} /> : null}
                  </p>
                  <p>
                    <label>Gender:</label>
                    {childEntity.gender}
                  </p>
                  <p>
                    <label>Weight:</label>
                    {childEntity.description ? childEntity.description.weight : ''}
                  </p>
                  <p>
                    <label>Height:</label>
                    {childEntity.description ? childEntity.description.height : ''}
                  </p>
                </Col>
                <Col lg={6}>
                  <p>
                    <label>Current age:</label>
                    {childEntity.age}
                  </p>
                  <p>
                    <label>Eye Color:</label>
                    {childEntity.description ? childEntity.description.eyeColor : ''}
                  </p>
                  <p>
                    <label>Hair Color:</label>
                    {childEntity.description ? childEntity.description.hairColor : ''}
                  </p>
                </Col>
              </Row>
            </Col>
            <Col lg={6}>
              <div className="child-info">
                <p>
                  <label>
                    <Translate contentKey="lostChildApp.child.address">Address</Translate>:{' '}
                  </label>
                  {childEntity.address ? `${childEntity.address.city}, ${childEntity.address.state}, ${childEntity.address.country}` : ''}
                </p>
                <p>
                  <label>
                    <Translate contentKey="lostChildApp.child.status">Status</Translate>:{' '}
                  </label>
                  {childEntity.status}
                </p>
                <p>
                  <label>Contact information:</label>
                  <b>{childEntity.contactInformation ? childEntity.contactInformation.email : ' '}</b>
                  <span>
                    <b>
                      {childEntity.contactInformation
                        ? ` ${childEntity.contactInformation.name}, [${childEntity.contactInformation.phoneNumber}]`
                        : ''}
                    </b>
                  </span>
                </p>
                <div className="child-last-seen-details">
                  <p>
                    <label>Last seen History:</label>
                  </p>
                </div>
              </div>
            </Col>
            <div className="details-btns">
              <Button tag={Link} to="/child" replace color="info" data-cy="entityDetailsBackButton">
                <FontAwesomeIcon icon="arrow-left" />{' '}
                <span className="d-none d-sm-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              {isAdmin && (
                <Button tag={Link} to={`/child/${childEntity.id}/edit`} replace color="primary">
                  <FontAwesomeIcon icon="pencil-alt" />{' '}
                  <span className="d-none d-sm-inline">
                    <Translate contentKey="entity.action.edit">Edit</Translate>
                  </span>
                </Button>
              )}
            </div>
          </Row>
        </CardBody>
      </Card>
    </div>
  );
};

export default ChildDetail;
