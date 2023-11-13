import {useKeycloak} from "@react-keycloak/web";
import {UserRoles} from "../../model/UserRoles.ts";
import React from "react";

interface PrivateComponentProps {
  allowedRoles: UserRoles[],
  children: React.ReactNode
}

export default ({allowedRoles, children}: PrivateComponentProps) => {

  const {keycloak} = useKeycloak();

  const isUserAllowed = allowedRoles.some(role => keycloak?.hasRealmRole(role));

  return isUserAllowed ? <>{children}</> : null;
}