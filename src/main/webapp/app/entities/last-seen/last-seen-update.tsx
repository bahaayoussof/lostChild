import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ILastSeenAddress } from 'app/shared/model/last-seen-address.model';
import { getEntities as getLastSeenAddresses } from 'app/entities/last-seen-address/last-seen-address.reducer';
import { IChild } from 'app/shared/model/child.model';
import { getEntities as getChildren } from 'app/entities/child/child.reducer';
import { getEntity, updateEntity, createEntity, reset } from './last-seen.reducer';
import { ILastSeen } from 'app/shared/model/last-seen.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const LastSeenUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const lastSeenAddresses = useAppSelector(state => state.lastSeenAddress.entities);
  const children = useAppSelector(state => state.child.entities);
  const lastSeenEntity = useAppSelector(state => state.lastSeen.entity);
  const loading = useAppSelector(state => state.lastSeen.loading);
  const updating = useAppSelector(state => state.lastSeen.updating);
  const updateSuccess = useAppSelector(state => state.lastSeen.updateSuccess);
  const handleClose = () => {
    props.history.push('/last-seen');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getLastSeenAddresses({}));
    dispatch(getChildren({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...lastSeenEntity,
      ...values,
      lastSeenAddress: lastSeenAddresses.find(it => it.id.toString() === values.lastSeenAddress.toString()),
      child: children.find(it => it.id.toString() === values.child.toString()),
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
          ...lastSeenEntity,
          lastSeenAddress: lastSeenEntity?.lastSeenAddress?.id,
          child: lastSeenEntity?.child?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="lostChildApp.lastSeen.home.createOrEditLabel" data-cy="LastSeenCreateUpdateHeading">
            <Translate contentKey="lostChildApp.lastSeen.home.createOrEditLabel">Create or edit a LastSeen</Translate>
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
                  id="last-seen-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('lostChildApp.lastSeen.date')}
                id="last-seen-date"
                name="date"
                data-cy="date"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="last-seen-lastSeenAddress"
                name="lastSeenAddress"
                data-cy="lastSeenAddress"
                label={translate('lostChildApp.lastSeen.lastSeenAddress')}
                type="select"
              >
                <option value="" key="0" />
                {lastSeenAddresses
                  ? lastSeenAddresses.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="last-seen-child"
                name="child"
                data-cy="child"
                label={translate('lostChildApp.lastSeen.child')}
                type="select"
              >
                <option value="" key="0" />
                {children
                  ? children.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/last-seen" replace color="info">
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

export default LastSeenUpdate;
