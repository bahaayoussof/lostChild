import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ILastSeen } from 'app/shared/model/last-seen.model';
import { getEntities as getLastSeens } from 'app/entities/last-seen/last-seen.reducer';
import { getEntity, updateEntity, createEntity, reset } from './last-seen-address.reducer';
import { ILastSeenAddress } from 'app/shared/model/last-seen-address.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const LastSeenAddressUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const lastSeens = useAppSelector(state => state.lastSeen.entities);
  const lastSeenAddressEntity = useAppSelector(state => state.lastSeenAddress.entity);
  const loading = useAppSelector(state => state.lastSeenAddress.loading);
  const updating = useAppSelector(state => state.lastSeenAddress.updating);
  const updateSuccess = useAppSelector(state => state.lastSeenAddress.updateSuccess);
  const handleClose = () => {
    props.history.push('/last-seen-address');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getLastSeens({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...lastSeenAddressEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...lastSeenAddressEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="lostChildApp.lastSeenAddress.home.createOrEditLabel" data-cy="LastSeenAddressCreateUpdateHeading">
            <Translate contentKey="lostChildApp.lastSeenAddress.home.createOrEditLabel">Create or edit a LastSeenAddress</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="last-seen-address-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('lostChildApp.lastSeenAddress.street')}
                id="last-seen-address-street"
                name="street"
                data-cy="street"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('lostChildApp.lastSeenAddress.city')}
                id="last-seen-address-city"
                name="city"
                data-cy="city"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('lostChildApp.lastSeenAddress.state')}
                id="last-seen-address-state"
                name="state"
                data-cy="state"
                type="text"
              />
              <ValidatedField
                label={translate('lostChildApp.lastSeenAddress.country')}
                id="last-seen-address-country"
                name="country"
                data-cy="country"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/last-seen-address" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default LastSeenAddressUpdate;
