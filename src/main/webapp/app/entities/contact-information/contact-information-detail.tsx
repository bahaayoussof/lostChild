import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './contact-information.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ContactInformationDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const contactInformationEntity = useAppSelector(state => state.contactInformation.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="contactInformationDetailsHeading">
          <Translate contentKey="lostChildApp.contactInformation.detail.title">ContactInformation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{contactInformationEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="lostChildApp.contactInformation.name">Name</Translate>
            </span>
          </dt>
          <dd>{contactInformationEntity.name}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="lostChildApp.contactInformation.email">Email</Translate>
            </span>
          </dt>
          <dd>{contactInformationEntity.email}</dd>
          <dt>
            <span id="phoneNumber">
              <Translate contentKey="lostChildApp.contactInformation.phoneNumber">Phone Number</Translate>
            </span>
          </dt>
          <dd>{contactInformationEntity.phoneNumber}</dd>
        </dl>
        <Button tag={Link} to="/contact-information" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/contact-information/${contactInformationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ContactInformationDetail;
